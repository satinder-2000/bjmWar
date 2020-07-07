/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bjm.test.embed;

import org.apache.tomee.embedded.Configuration;
import org.apache.tomee.embedded.Container;

/**
 *
 * @author root
 */
public class TomeeEmbed {
    
    public static void main(String... args){
        
        Container container=new Container(new Configuration());
        container.deployClasspathAsWebApp();
        System.out.println("Started on http://localhost:" + container.getConfiguration().getHttpPort());
    }
    
}
