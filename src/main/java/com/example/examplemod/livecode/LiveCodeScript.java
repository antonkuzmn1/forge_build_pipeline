package com.example.examplemod.livecode;

import java.util.List;

public final class LiveCodeScript {
    public interface LiveCodeAction {
        void execute(LiveCodeRuntime.Context ctx);
    }

    public record TickTask(int intervalTicks, LiveCodeAction action) {
        public TickTask {
            if (intervalTicks <= 0) {
                throw new IllegalArgumentException("intervalTicks must be > 0");
            }
        }
    }

    private final List<TickTask> tickTasks;
    private final List<LiveCodeAction> onReload;
    private final List<LiveCodeAction> once;

    public LiveCodeScript(List<TickTask> tickTasks, List<LiveCodeAction> onReload, List<LiveCodeAction> once) {
        this.tickTasks = List.copyOf(tickTasks);
        this.onReload = List.copyOf(onReload);
        this.once = List.copyOf(once);
    }

    public List<TickTask> tickTasks() {
        return tickTasks;
    }

    public List<LiveCodeAction> onReload() {
        return onReload;
    }

    public List<LiveCodeAction> once() {
        return once;
    }
}
