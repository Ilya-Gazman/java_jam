package com.gazman.processor;

/**
 * Created by ilyagazman on 4/12/15.
 */
public class OutputManager {

    private OutputCallback callback;

    public OutputManager(OutputCallback callback) {
        this.callback = callback;
    }

    public void print(Object... items) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < items.length; i++) {
            if (i > 0) {
                stringBuilder.append(" ");
            }
            Object item = items[i];
            stringBuilder.append(item);
        }
        String message = stringBuilder.toString();
        callback.onOutput(message);
    }

    public void print(int[][] map, String[] conversion) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < map.length; i++) {
            if (i > 0) {
                stringBuilder.append("\n");
            }
            for (int j = 0; j < map[i].length; j++) {
                stringBuilder.append(conversion[map[i][j]]);
            }
        }
        print(stringBuilder);
    }
}
