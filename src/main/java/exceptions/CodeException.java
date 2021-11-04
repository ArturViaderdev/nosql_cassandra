/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package exceptions;

/**
 *
 * @author alu2014080
 */
public abstract class CodeException extends Exception {

    private final int code;

    public CodeException(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

}
