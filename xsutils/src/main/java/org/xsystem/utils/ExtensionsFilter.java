/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.xsystem.utils;

import java.io.File;
import java.io.FileFilter;

/**
 *
 * @author tim
 */

public class ExtensionsFilter implements FileFilter 
{
    private final char[][] extensions;

    public ExtensionsFilter(String[] extensions)
    {
        int length = extensions.length;
        this.extensions = new char[length][];
        for (String s : extensions)
        {
            this.extensions[--length] = s.toCharArray();
        }
    }

    @Override
    public boolean accept(File file)
    {
        char[] path = file.getPath().toCharArray();
        for (char[] extension : extensions)
        {
            if (extension.length > path.length)
            {
                continue;
            }
            int pStart = path.length - 1;
            int eStart = extension.length - 1;
            boolean success = true;
            for (int i = 0; i <= eStart; i++)
            {
                if ((path[pStart - i] | 0x20) != (extension[eStart - i] | 0x20))
                {
                    success = false;
                    break;
                }
            }
            if (success)
                return true;
        }
        return false;
    }

    
}
