package com.example.examplemod.livecode;

import java.util.ArrayList;
import java.util.List;

public final class LiveCodeParser {
    public record Result(LiveCodeScript script, List<String> errors) {
    }

    private LiveCodeParser() {
    }

    public static Result parse(String source) {
        List<String> errors = new ArrayList<>();
        List<LiveCodeScript.TickTask> tickTasks = new ArrayList<>();
        List<LiveCodeScript.LiveCodeAction> onReload = new ArrayList<>();
        List<LiveCodeScript.LiveCodeAction> once = new ArrayList<>();

        if (source == null) {
            source = "";
        }

        String[] lines = source.replace("\r", "").split("\n", -1);
        for (int i = 0; i < lines.length; i++) {
            String raw = lines[i];
            String line = raw.trim();
            if (line.isEmpty()) {
                continue;
            }

            ParsedLine pl = ParsedLine.parse(line);
            if (pl == null) {
                errors.add("Line " + (i + 1) + ": cannot parse");
                continue;
            }

            switch (pl.kind) {
                case EVERY -> {
                    if (pl.intervalTicks <= 0) {
                        errors.add("Line " + (i + 1) + ": interval must be > 0");
                        continue;
                    }
                    LiveCodeScript.LiveCodeAction action = parseAction(i + 1, pl.actionKind, pl.payload, errors);
                    if (action != null) {
                        tickTasks.add(new LiveCodeScript.TickTask(pl.intervalTicks, action));
                    }
                }
                case ON_RELOAD -> {
                    LiveCodeScript.LiveCodeAction action = parseAction(i + 1, pl.actionKind, pl.payload, errors);
                    if (action != null) {
                        onReload.add(action);
                    }
                }
                case ONCE -> {
                    LiveCodeScript.LiveCodeAction action = parseAction(i + 1, pl.actionKind, pl.payload, errors);
                    if (action != null) {
                        once.add(action);
                    }
                }
            }
        }

        LiveCodeScript script = errors.isEmpty() ? new LiveCodeScript(tickTasks, onReload, once) : null;
        return new Result(script, List.copyOf(errors));
    }

    private static LiveCodeScript.LiveCodeAction parseAction(int lineNo, ActionKind kind, String payload, List<String> errors) {
        if (payload == null) {
            payload = "";
        }
        String p = payload.trim();
        if (p.isEmpty()) {
            errors.add("Line " + lineNo + ": empty action payload");
            return null;
        }

        return switch (kind) {
            case MSG -> ctx -> ctx.notify(p);
            case RUN -> ctx -> ctx.runCommand(p);
        };
    }

    private enum StmtKind {
        EVERY,
        ON_RELOAD,
        ONCE
    }

    private enum ActionKind {
        MSG,
        RUN
    }

    private record ParsedLine(StmtKind kind, int intervalTicks, ActionKind actionKind, String payload) {
        static ParsedLine parse(String line) {
            String[] t = splitFirstTokens(line, 3);
            if (t == null) {
                return null;
            }

            String head = t[0];
            if ("every".equalsIgnoreCase(head)) {
                if (t[1] == null || t[2] == null) {
                    return null;
                }
                int interval;
                try {
                    interval = Integer.parseInt(t[1]);
                } catch (NumberFormatException e) {
                    return null;
                }
                ActionKind ak = parseActionKind(t[2]);
                if (ak == null) {
                    return null;
                }
                String payload = restAfterTokens(line, 3);
                return new ParsedLine(StmtKind.EVERY, interval, ak, payload);
            }

            if ("on_reload".equalsIgnoreCase(head)) {
                if (t[1] == null) {
                    return null;
                }
                ActionKind ak = parseActionKind(t[1]);
                if (ak == null) {
                    return null;
                }
                String payload = restAfterTokens(line, 2);
                return new ParsedLine(StmtKind.ON_RELOAD, 0, ak, payload);
            }

            if ("once".equalsIgnoreCase(head)) {
                if (t[1] == null) {
                    return null;
                }
                ActionKind ak = parseActionKind(t[1]);
                if (ak == null) {
                    return null;
                }
                String payload = restAfterTokens(line, 2);
                return new ParsedLine(StmtKind.ONCE, 0, ak, payload);
            }

            return null;
        }

        private static ActionKind parseActionKind(String s) {
            if (s == null) {
                return null;
            }
            if ("msg".equalsIgnoreCase(s)) {
                return ActionKind.MSG;
            }
            if ("run".equalsIgnoreCase(s)) {
                return ActionKind.RUN;
            }
            return null;
        }

        private static String[] splitFirstTokens(String line, int maxTokens) {
            if (maxTokens <= 0) {
                return null;
            }
            String[] out = new String[maxTokens];
            int idx = 0;
            int i = 0;
            while (i < line.length() && idx < maxTokens) {
                while (i < line.length() && Character.isWhitespace(line.charAt(i))) {
                    i++;
                }
                if (i >= line.length()) {
                    break;
                }
                int start = i;
                while (i < line.length() && !Character.isWhitespace(line.charAt(i))) {
                    i++;
                }
                out[idx++] = line.substring(start, i);
            }
            return out;
        }

        private static String restAfterTokens(String line, int tokenCount) {
            int i = 0;
            int tokens = 0;
            while (i < line.length()) {
                while (i < line.length() && Character.isWhitespace(line.charAt(i))) {
                    i++;
                }
                if (i >= line.length()) {
                    return "";
                }
                int start = i;
                while (i < line.length() && !Character.isWhitespace(line.charAt(i))) {
                    i++;
                }
                tokens++;
                if (tokens == tokenCount) {
                    while (i < line.length() && Character.isWhitespace(line.charAt(i))) {
                        i++;
                    }
                    if (i >= line.length()) {
                        return "";
                    }
                    return line.substring(i);
                }
                if (start == i) {
                    break;
                }
            }
            return "";
        }
    }
}
