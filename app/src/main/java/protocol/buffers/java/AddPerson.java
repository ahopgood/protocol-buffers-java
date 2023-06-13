package protocol.buffers.java;

import protocol.buffers.java.model.AddressBook;
import protocol.buffers.java.model.Person;

import java.io.*;

public class AddPerson {
    static Person PromptForAddress(BufferedReader stdin, PrintStream stdout) throws IOException {

        var person = Person.newBuilder();
        stdout.println("Enter person ID: ");
        person.setId(Integer.valueOf(stdin.readLine()));

        stdout.println("Enter email address (blank for none): ");
        String email = stdin.readLine();
        if (email.length() > 0) {
            person.setName(email);
        }

        while (true) {
            stdout.println("Enter a phone number (or leave blank to finish): ");
            String number = stdin.readLine();
            if (number.length() == 0) {
                break;
            }

            var phoneNumber = Person.PhoneNumber.newBuilder();
            stdout.print("Is this a mobile, home, or work phone? ");
            String type = stdin.readLine();;
            if (type.equals("mobile")) {
                phoneNumber.setType(Person.PhoneType.MOBILE);
            } else if (type.equals("home")) {
                phoneNumber.setType(Person.PhoneType.HOME);
            } else if (type.equals("work")) {
                phoneNumber.setType(Person.PhoneType.WORK);
            } else {
                stdout.println("Unknown phone type. Using default.");
            }
            person.addPhones(phoneNumber);
        }

        return person.build();
    }

    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            System.err.println("Usage: AddPerson ADDRESS_BOOK_FILE");
            System.exit(-1);
        }

        var addressBook = AddressBook.newBuilder();
        try {
            addressBook.mergeFrom(new FileInputStream(args[0]));

        } catch (FileNotFoundException fnfe) {
            System.out.println(args[0] + ": File not found. Creating a new file.");
        }
        // Add an address
        addressBook.addPeople(
                PromptForAddress(new BufferedReader(new InputStreamReader(System.in)), System.out)
        );

        // Write the new address book back to disk
        FileOutputStream output = new FileOutputStream(args[0]);
        addressBook.build().writeTo(output);
        output.close();
    }
}
