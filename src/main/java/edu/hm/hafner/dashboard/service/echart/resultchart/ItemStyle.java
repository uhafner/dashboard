package edu.hm.hafner.dashboard.service.echart.resultchart;

/**
 * Model to define the item style in the ui.
 */
public class ItemStyle {
    @SuppressWarnings("checkstyle:JavadocVariable")
    public static final String TRANSPARENT = "transparent";
    private final String barBorderColor;
    private final String color;

    /**
     * Creates a new instance of {@link ItemStyle}.
     *
     * @param barBorderColor the bar border color
     * @param color          the main color
     */
    public ItemStyle(final String barBorderColor, final String color) {
        this.barBorderColor = barBorderColor;
        this.color = color;
    }

    /**
     * Returns the bar border color of the {@link ItemStyle}.
     *
     * @return the bar border color
     */
    public String getBarBorderColor() {
        return barBorderColor;
    }

    /**
     * Returns the color of the {@link ItemStyle}.
     *
     * @return the color
     */
    public String getColor() {
        return color;
    }
}
