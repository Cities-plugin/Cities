package nl.partytitan.cities.messageformats;

import nl.partytitan.cities.helpers.DefaultFontInfo;
import org.bukkit.ChatColor;

public class GlobalFormatters {
    private final static int CENTER_PX = 154;

    public static ChatColor PrimaryColor = ChatColor.DARK_AQUA;
    public static ChatColor SecondaryColor = ChatColor.DARK_GRAY;
    public static ChatColor AccentColor = ChatColor.GOLD;
    public static ChatColor TitleColor = ChatColor.WHITE;

    public static String formatTitle(String title) {
        String pre = SecondaryColor + "Oo" + PrimaryColor;
        String post = SecondaryColor + "oO";
        char lineChar = '-';

        String centerMessage = SecondaryColor +"{ " + TitleColor + title + SecondaryColor +" }" + PrimaryColor;
        int toCompensate = CENTER_PX - (getStringPxLength(pre + centerMessage + post) / 2);

        String spacer = fillPxWithChar(lineChar, toCompensate);
        return pre + spacer + centerMessage + spacer + post;
    }

    private static int getStringPxLength(String message){
        int messagePxSize = 0;
        boolean previousCode = false;
        boolean isBold = false;

        for(char c : message.toCharArray()){
            if(c == '§'){
                previousCode = true;
                continue;
            }else if(previousCode == true){
                previousCode = false;
                if(c == 'l' || c == 'L'){
                    isBold = true;
                    continue;
                }else isBold = false;
            }else{
                DefaultFontInfo dFI = DefaultFontInfo.getDefaultFontInfo(c);
                messagePxSize += isBold ? dFI.getBoldLength() : dFI.getLength();
                messagePxSize++;
            }
        }
        return messagePxSize;
    }

    private static String fillPxWithChar(char fillChar, int pxLength){
        int lineLength = DefaultFontInfo.getDefaultFontInfo(fillChar).getLength() + 1;
        int compensated = 0;
        StringBuilder sb = new StringBuilder();
        while(compensated < pxLength){
            sb.append(fillChar);
            compensated += lineLength;
        }

        return sb.toString();
    }
}
