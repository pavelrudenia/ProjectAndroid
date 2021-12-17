package rudenia.fit.bstu.projectstpms.model;

public class Notes {

    private int color;
    private String categoryName;
    private String sumCosts;


    public Notes() {

    }

    public Notes(int color, String categoryName, String sumCosts) {
        this.color = color;
        this.categoryName = categoryName;
        this.sumCosts = sumCosts;
    }


    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getSumCosts() {
        return sumCosts;
    }

    public void setSumCosts(String sumCosts) {
        this.sumCosts = sumCosts;
    }
}
