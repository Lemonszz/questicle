package party.lemons.questicle.util;

public class QMath
{
    public static double round2(double number, int scale) {
        int pow = 10;
        for (int i = 1; i < scale; i++)
            pow *= 10;
        double tmp = number * pow;
        return ( (double) ( (int) ((tmp - (int) tmp) >= 0.5f ? tmp + 1 : tmp) ) ) / pow;
    }

    public static boolean inArea(int x, int y, int minX, int minY, int maxX, int maxY)
    {
        return x >= minX && y >= minY && x < maxX && y < maxY;
    }
}
