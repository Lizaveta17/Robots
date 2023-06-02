package logic;

public enum GameState {
    SCORE_CHANGED("SCORE_CHANGED"),
    GAME_WIN("GAME_WIN"),
    GAME_OVER("GAME_OVER");


    public final String state;

    GameState(String state) {
        this.state = state;
    }
}
