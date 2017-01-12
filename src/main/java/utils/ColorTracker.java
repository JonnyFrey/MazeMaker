package utils;

import java.awt.*;

/**
 * Created by Jonny on 1/12/17.
 */
public class ColorTracker {

    private Color from;
    private Color to;
    private boolean rainbow;

    private int maxDistance;

    public ColorTracker(Color from, Color to) {
        this.from = from;
        this.to = to;
        this.rainbow = true;
    }

    public Color getColorFromFloat(int dist) {
        if (rainbow) {
            float hue = dist / (this.maxDistance * 1.2f);
            return new Color(Color.HSBtoRGB(hue, 1f, 1f));
        } else {
            float blending = dist / (float)(this.maxDistance);

            float inverse_blending = 1 - blending;

            float red =   to.getRed()   * blending   +   from.getRed()   * inverse_blending;
            float green = to.getGreen() * blending   +   from.getGreen() * inverse_blending;
            float blue =  to.getBlue()  * blending   +   from.getBlue()  * inverse_blending;

            return new Color (red / 255, green / 255, blue / 255);
        }
    }

    public void setFrom(Color from) {
        if (from != null) {
            this.from = from;
        }
    }

    public void setTo(Color to) {
        if (to != null) {
            this.to = to;
        }
    }

    public void setMaxDistance(int maxDistance) {
        this.maxDistance = maxDistance;
    }

    public void setRainbow(boolean rainbow) {
        this.rainbow = rainbow;
    }
}
