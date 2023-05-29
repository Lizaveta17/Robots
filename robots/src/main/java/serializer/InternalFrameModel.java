package serializer;

public class InternalFrameModel {
    public InternalFrameModel(String title, int width, int height, int x,  int y, boolean icon){
        this.title = title;
        this.width = width;
        this.height = height;
        this.x = x;
        this.y = y;
        this.icon = icon;
    }
    public String title;
    public int width;
    public int height;
    public int x;
    public int y;
    public boolean icon;

}
