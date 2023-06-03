package logic.entity;

public enum GameState {
    SCORE_CHANGED("SCORE_CHANGED"),
    GAME_END("GAME_END");


    public final String state;

    GameState(String state) {
        this.state = state;
    }
}
