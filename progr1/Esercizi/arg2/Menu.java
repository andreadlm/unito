package arg2;

import libs.SIn;

class Menu {
    public static void main(String[] args) {
        
        char opt;
        
        System.out.print("Options:\n\ta. New document\n\tb. Open document\n\tc. Save as ...\n\td. Mail document to ...\nPlease, make your choice (a-d): ");
        opt = SIn.readChar();

        if(opt == 'a') System.out.println("You chose to create a new document");
        else if(opt == 'b') System.out.println("You chose to open a document");
        else if(opt == 'c') System.out.println("You chose to save the current document");
        else if(opt == 'd') System.out.println("You chose to send the current document via e-mail");
        else System.out.println("Your choice is not valid");
    }
}