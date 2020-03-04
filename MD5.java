
import java.util.*;
import java.io.*;
import javax.xml.bind.DatatypeConverter;
import java.security.NoSuchAlgorithmException;
import java.security.MessageDigest;
/**
 * MD5
 * Kelbin Rodriguez and Matthew McDonald
 * CS 492
 * 04.25.2018
 */
public class MD5
{
    private String password;
    String hash;
    int value;
    boolean shortcut;
    public MD5(String pw, boolean shortcut)
    {
        password = pw;
        this.hash = null;
        this.value = 32;
        this.shortcut = false;
        //every new MD5 object has a default shotcut of false, this later can be changed if the user prompts that there is a shortcut. 
    }

    public String hashFunction(MD5 obj)
    {
        try {

            MessageDigest md5 = MessageDigest.getInstance("MD5"); 
            //creates md5 instance for hasing passwords
            md5.update(obj.password.getBytes());
            //gets an encoded string of bytes
            byte[] digest = md5.digest();
            //bytes are placed into a byte array.
            obj.hash = DatatypeConverter.printHexBinary(digest).toUpperCase();
            //every byte value in digest is then converted to a hexadecimal value, with all chacacter values being converted to an upper case 
            //value as well. This allows for the proper format of a hash. The hash is then placed into the hash string. 
            return obj.hash;
            //hash is returned. 
        }
        catch(NoSuchAlgorithmException e)
        {
            throw new RuntimeException(e);
            //Throws a runtime exception witjh null as the message.
            //What causes this exception to be thrown is there are no more numbers to be enumerated. 
        }
    }

    public void bForce(MD5 pass, String known, int position)
    {
        MD5[] trial = new MD5[6];
        boolean found = false;
        boolean quit = false;
        int quitIndex = 0;
        
        for(int i = 0; i < 6; i++)
        {
            trial[i] = new MD5(null, false);
            //goes through every index of trial and sets password to null and shortcut to null
        }
        
        if(known != null)
        {
            //if a known string is given the following block will exacute.
            MD5[] shortArray = createShort(known, position);
            //creates a new md5 object array calling createShort, this allows the certain positions of the password that do not need to be checked
            //this allows for the decryption to run faster as less charcaters need to be guessed. 
            for(int i =0; i<6; i++)
            {
                trial[i] = shortArray[i];
                //sets the trial[i] index with the shortArray[i] values. 
            }
            /*
            quitIndex = 6;
            for (int i = 0; i < trial.length; i++)
            {
                if(trial[i].shortcut == false && i < quitIndex)
                {
                    quitIndex = i;
                }
                //
            }*/
        }
        
        //if the user did not enter a known charcter string that is within the passwor, the following while loops will go through each position
        //if the entire guess does not match the password hash it is trying to crack the ASCII values of the guess will increment. Each guess will 
        //traverse through capital letters, lower case letters, numbers, and symbols. 
        
        while(trial[0].value < 126 && found == false && quit == false)
        {
            if(trial[0].shortcut == false)
            {
                trial[0].value++;
            }
            if(trial[1].shortcut == false)
            {
                trial[1].value = 32;
            }
            if(quitIndex == 0)
            {
                checkQuit(trial, 0);
            }

            while(trial[1].value < 126 && found == false)
            {
                if(trial[1].shortcut == false)
                {
                    trial[1].value++;
                }
                if(trial[2].shortcut == false)
                {
                    trial[2].value = 32;
                }
                if(quitIndex == 1)
                {
                    checkQuit(trial, 1);
                }
                while(trial[2].value < 126 && found == false)
                {
                    if(trial[2].shortcut == false)
                    {
                        trial[2].value++;
                    }
                    if(trial[3].shortcut == false)
                    {
                        trial[3].value = 32;
                    }
                    if(quitIndex == 2)
                    {
                        checkQuit(trial, 2);
                    }
                    while(trial[3].value < 126 && found == false)
                    {
                        if(trial[3].shortcut == false)
                        {
                            trial[3].value++;
                        }
                        if(trial[4].shortcut == false)
                        {
                            trial[4].value = 32;
                        }
                        if(quitIndex == 3)
                        {
                            checkQuit(trial, 3);
                        }
                        while(trial[4].value < 126 && found == false)
                        {
                            if(trial[4].shortcut == false)
                            {
                                trial[4].value++;
                            }
                            if(trial[5].shortcut == false)
                            {
                                trial[5].value = 32;
                            }
                            if(quitIndex == 4)
                            {
                                checkQuit(trial, 4);
                            }
                            while(trial[5].value < 126 && found == false)
                            {
                                if(trial[5].shortcut == false)
                                {
                                    trial[5].value++;
                                }
                                if(quitIndex == 5)
                                {
                                    checkQuit(trial, 5);
                                }
                                MD5 guess = new MD5(getString(trial), false);
                                //System.out.println(guess.password);
                                found = compareHash(guess, pass);
                                //the has is guess hass is compared to the actual password hash. If they match the program will out put the cracked
                                //password and its hash. 
                                //else the while loops will continue to progress untill the hash matches. 
                                if(found == true)
                                {
                                    System.out.println("Password was cracked: "+guess.password+" "+guess.hash);
                                }
                            }
                        }
                    }
                }
            }
        }

    }

    public boolean compareHash(MD5 obj, MD5 pass)
    {
        String trialHash = hashFunction(obj);
        if(trialHash.equals(pass.hash))
        {
            return true;
        }
        else
        {
            return false; 
        }
        //fucntion will compare two hashes. The hash of the password and the hash of the cracker. In the case with MD5 there can be some instances
        //where the hash of the cracker can exactly the same and as the password but the attempted password entered can be dfferent than the actual
        //password. This weakness is called hash collisions. 
    }

    public String getString(MD5[] list)
    {
        String guess = "";
        char c;
        for(int i=0; i<6; i++)
        {
            c = (char)list[i].value;
            guess += c;
        }
        return guess;
        //Returns the converted ASCII value into a string. This would allow users to view which current passwords that the program is checking
        //that may match with hash of the actual password. 
    }

    public MD5[] createShort(String input, int position)
    {
        char[] temp = input.toCharArray();
        MD5[] shortArray = new MD5[6];
        for (int i=0; i<6; i++)
        {
            shortArray[i] = new MD5(null, false);
        }
        int n = 0;
        for(int i = position; i<input.length(); i++)
        {
            shortArray[i].value = (int)temp[n];
            shortArray[i].shortcut = true;
            n++;
        }
        return shortArray;
        //takes the known user shortcut and puts it into a character array using the first array, setting a new md5 object.
        //The second for loop will set I at the starting position that the user enters. The loop will then go through and set shortArray[i]'s
        //value to the ACII value of character. The shortcut is then set to true so the decryption algorithm knows not to check it while it is
        //going through the brute force process. 
    }

    public boolean checkQuit(MD5[] trial, int index)
    {
        boolean exit = false;
        if(trial[index].value == 125)
        {
            exit = true;
        }
        return exit;
        //if the short cut wasn't found it would exit the loop after the brute force had been completed. Currently not working. 
    }

}
