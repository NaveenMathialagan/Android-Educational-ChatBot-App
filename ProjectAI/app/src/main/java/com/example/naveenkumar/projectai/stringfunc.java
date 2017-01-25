package com.example.naveenkumar.projectai;

import android.util.Log;

/**
 * Created by Naveen kumar on 16-10-2016.
 */

public class stringfunc{

    String res="";
    int start=0;
    String result="";
    public stringfunc(){

    }
    public String alter(String resul,String key){
        result=resul;
        boolean detec=false;
        System.out.println("came to alter");
        result=result.trim();
        result=result.replace("[[","");
        result=result.replace("]]","");
        result=result.replace("<br />","");
        result=result.replace("<br/>","");
        result=result.replace("&nbsp;","");
        result=result.trim();

        if (result.contains("\'\'\'"+key+"\'\'\'")){
            Log.d("nav","Result contains th key");
        try {
            result=result.replace(result.substring(0,result.indexOf("\'\'\'"+key+"\'\'\'")),"");
            Log.d("nav","key detected");
            detec=true;
        }catch (Exception e){

            System.out.println("Execptopn 4\t"+e);
        }
        }
        if (detec){

        }else{
            String[] ary=key.split(" ");
            if (result.contains("\'\'\'"+ary[0])){
            result=result.substring(result.indexOf("\'\'\'"+ary[0]),result.length());
            System.out.print("Substring created");}else{

            }
        }
        while(true){
            try{
                if(result.contains("<ref")){
                    System.out.println(result.indexOf("/>")+"\t"+result.indexOf("</ref>"));
                    if(result.indexOf("/>")==(-1)){
                       // System.out.println(s2.substring(s2.indexOf("<ref"),s2.indexOf("</ref>")+6));
                        result=result.replace(result.substring(result.indexOf("<ref"),result.indexOf("</ref>")+6),"");
                       // System.out.println(s2);
                    }else if(result.indexOf("</ref>")==(-1)){
                       // System.out.println(s2.substring(s2.indexOf("<ref"),s2.indexOf("/>")+2));
                        result=result.replace(result.substring(result.indexOf("<ref"),result.indexOf("/>")+2),"");
                       // System.out.println(s2);
                    }else{
                        if(result.indexOf("/>")<result.indexOf("</ref>")){
                         //   System.out.println(s2.substring(s2.indexOf("<ref"),s2.indexOf("/>")+2));
                            result=result.replace(result.substring(result.indexOf("<ref"),result.indexOf("/>")+2),"");
                           // System.out.println(s2);
                        }else{
                          //  System.out.println(s2.substring(s2.indexOf("<ref"),s2.indexOf("</ref>")+6));
                            result=result.replace(result.substring(result.indexOf("<ref"),result.indexOf("</ref>")+6),"");
                           // System.out.println(s2);
                        }
                    }
                }else{
                    break;
                }
            }catch(Exception e){
                System.out.println("Execptopn 2\t"+e);
                break;
            }
        }
        while(true){
            try{
                if(result.contains("{{")){
                    result=result.replace(result.substring(result.indexOf("{{"), result.indexOf("}}")+2), "");
                }else{
                    break;
                }
            }catch (Exception e){
                System.out.println("Execptopn 1\t"+e);
                break;
            }
        }

        result=result.trim();

        while(true){
            try {
            if(result.contains("(")){
                result=result.replace(result.substring(result.indexOf("("), result.indexOf(")")+1), "");
            }else{
                break;
            }
            }
            catch (Exception e){
                System.out.println("Execptopn 3\t"+e);
                break;
            }
        }
        result=result.trim();
        result=result.replace("\'\'","");
        result=result.replace("\'\'","");
     /*   if(result.contains("==See also==")){
            result=result.replace(result.substring(result.indexOf("==See also=="),result.length()),"");
        }
        if(result.contains("==References==")){
            result=result.replace(result.substring(result.indexOf("==References=="),result.length()),"");
        }
        result=result.trim();
*/

        while (true){
            try {
                if (result.contains("<!--")){
                    result=result.replace(result.substring(result.indexOf("<!--"),result.indexOf("-->")+3),"");

                }else{
                    break;
                }}
            catch (Exception e)
            {
                System.out.println("Execptopn 6\t"+e);
                break;
            }
        }
        while (true){
            try {
            if (result.contains("File:")){
                result=result.replace(result.substring(result.indexOf("File:"),result.indexOf("\n")),"");

            }else{
                break;
            }}
            catch (Exception e)
            {
                System.out.println("Execptopn 5\t"+e);
                break;
            }
        }
        result=result.replace("\n\n","\n");
        result=result.replaceAll("\\*"," ");
        result=result.replaceAll("\\=\\=","");
        result=result.replaceAll("\\=","");
        result=result.replaceAll("\\{\\{","");
        result=result.replaceAll("\\}\\}","");
        result=result.trim();
        return result;
    }
    }

