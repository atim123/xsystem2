/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.xsystem.sql2.http;

import java.io.File;
/**
 *
 * @author atimofeev
 */
@FunctionalInterface
public interface FileChageEvent {
    void onChange(File f);
}
