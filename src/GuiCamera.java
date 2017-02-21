/**
 * Created by 贾晓磊 on 2016/12/19.
 */
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;



class GuiCamera
{
    private String fileName; // 文件的前缀

    private String defaultName = "GuiCamera";

    static int serialNum = 0;

    private String imageFormat; // 图像文件的格式

    private String defaultImageFormat = "png";

    Dimension d = Toolkit.getDefaultToolkit().getScreenSize();

    public GuiCamera()
    {
        fileName = defaultName;
        imageFormat = defaultImageFormat;
    }


    public GuiCamera(String s, String format)
    {
        fileName = s;
        imageFormat = format;
    }


    public void snapShot()
    {
        try
        {

            BufferedImage screenshot = (new Robot())
                    .createScreenCapture(new Rectangle(0, 0,
                            (int) d.getWidth(), (int) d.getHeight()));
            serialNum++;

            String name = fileName + String.valueOf(serialNum) + "."
                    + imageFormat;
            File f = new File(name);
            System.out.print("Save File " + name);

            ImageIO.write(screenshot, imageFormat, f);
            System.out.print("..Finished! ");
        } catch (Exception ex)
        {
            System.out.println(ex);
        }
    }

}
