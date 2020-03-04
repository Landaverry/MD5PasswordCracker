import java.util.*;
import java.io.*;
import javax.xml.bind.DatatypeConverter;
import java.security.NoSuchAlgorithmException; //used for throwing an exception in the event that the encryption fails. 
import java.security.MessageDigest; //used for MD5 Encryption.
/**
 * bruteForce will prompt users to enter a password between 1-8 charcters
 * Kelbin Rodriguez and Matthew McDonald
 * CS 492
 * 04.25.2018
 */
public class bruteForce
{
    public static void main(String[]args) throws NoSuchAlgorithmException
    {
        Scanner scan = new Scanner(System.in);
        System.out.println("Please enter a 6 Character password");
        String password = scan.nextLine();
        //program has user enter a 6 charcter password. 
        while(password.length()!=6)
        {
            System.out.println("Please enter a 6 Character password");
            password = scan.nextLine();
            //person will continously be prompted to enter a 6 chacrter password when they do not enter a a 6 character password
        }
        MD5 encryption = new MD5(password, false);
        //creates a new MD5 Encryption instance.
        encryption.hash = encryption.hashFunction(encryption);
        //gets the has of the password entered. 
        System.out.println("Hash: " + encryption.hash);
        
        System.out.println("Type 'yes' if you would like to enter a known sequence of characters / type 'no' for complete brute force.");
        String userKnow = scan.nextLine();
        //asks and prompts user to enter a known string of the password. 
        if(userKnow.toLowerCase().equals("yes"))
        {
            System.out.println("Please carefully enter the known characters (case sensitive, no spaces, cannot be more than password length):");
            userKnow = scan.nextLine();
            //has user enter a known string to shortcut finding the hash of the passwords
            while(userKnow.length() > 6)
            {
                System.out.println("Please enter a length less than 6:");
                userKnow = scan.nextLine();
                //if the length of the string is more than 6 chacacters the user is prompted to enter another string
            }
            
            System.out.println("Please enter what position you'd like to test the shortcut. For example:"
                + "\nLet '!' represent characters to be brute forced, and the phrase 'know' to be what you enter." +
                "\nIf you want to test the following: '!!know!', then the position to enter would be 2.");
            int position = scan.nextInt();
            //has user enter the start position for the shortcut. the first chacacter being a value 0 and the last chacacter being a value of 5
            while(position + userKnow.length() > 6)
            {
                System.out.println("Please enter a position that fits your guess in the password length:");
                System.out.println("Your guess: " + userKnow);
                position = scan.nextInt();
                //if the user enters a value greater than 6 the user, the user will continue to be prompted to enter a value from 0 to 5.
            }
            encryption.bForce(encryption, userKnow, position);
            //encryption is called and will use the given string to attempt a shortcut
        }
        else
        {
            encryption.bForce(encryption, null, 0);
            //regular brute force will be conducted. 
        }
        
        
        
        
        //encryption.bForce(encryption);
        
    }
}
