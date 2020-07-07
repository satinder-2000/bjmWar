/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsoup;



/**
 *
 * @author root
 */
public class JSoupTest {
    
    public static void main(String[] args){
        //String htmlStr="<p class=\"ql-align-justify\"><span style=\"color: rgb(0, 0, 0); background-color: transparent;\">I have always felt that GDP is not the best possible indicator of any countryÃ¢ÂÂs economic health, including India.";
        String htmlStr="<p class=\"ql-align-justify\"><span style=\"color: rgb(0, 0, 0); background-color: transparent;\">I have always felt that GDP is not the best possible indicator of any countryÃ¢ÂÂs economic health, including India. "
                + "It only tells about the Ã¢ÂÂwealthÃ¢ÂÂ that we accumulate and nothing about its distribution or the quality of life that the majority of our people live in. Of course GDP can be raised if the top 10% of the population of India raise their percentage from 80.7%, (as of November 2016 - See https://en.wikipedia.org/wiki/Income_inequality_in_India) to say over 90 or 95%. But, would that be a good indicator? Of course not, unless we change something very significantly!</span></p><p><br></p><p><span style=\"color: rgb(0, 0, 0); background-color: transparent;\">I believe that India must adopt UN's Human Development Index (HDI) (</span><a href=\"http://hdr.undp.org/en/content/human-development-index-hdi\" rel=\"noopener noreferrer\" target=\"_blank\" style=\"color: rgb(17, 85, 204); background-color: transparent;\">http://hdr.undp.org/en/content/human-development-index-hdi</a><span style=\"color: rgb(0, 0, 0); background-color: transparent;\">). It does not advise to drop GDP as an economic development indicator-&nbsp;but propagates a culmination of GDP/GNI with a welfare index that includes poverty elimination, educating the masses and many more."
        + "</span></p>";
        //String result=htmlStr.replaceAll("\\<.*?\\>", "");
        
        String result=htmlStr;//Jsoup.clean(htmlStr, Whitelist.basic());
        result=result.replaceAll("\\<.*?>", "").replaceAll("&amp;","&").replaceAll("&gt;",">").replaceAll("&lt;","<").replaceAll("Â","#").replaceAll("Ã.{5}", "'").replaceAll("&nbsp;", " ");
        System.out.println(result);
        
        
    }
    
    
    
}
