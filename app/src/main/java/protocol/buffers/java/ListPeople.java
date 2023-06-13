package protocol.buffers.java;

import protocol.buffers.java.model.AddressBook;
import protocol.buffers.java.model.Person;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class ListPeople {

    static void Print(AddressBook addressBook) {
        for (Person person: addressBook.getPeopleList()) {
            System.out.println("Person ID: " + person.getId());
            System.out.println("Person Name: " + person.getName());
            if (person.hasEmail()) {
                System.out.println("  E-mail address: " + person.getEmail());
            }

            for (var phoneNumber : person.getPhonesList()) {
                switch (phoneNumber.getType()) {
                    case MOBILE -> System.out.print("   Mobile phone #: ");
                    case HOME -> System.out.println("   Home phone #: ");
                    case WORK -> System.out.println("   Work phone #: ");
                }
                System.out.println(phoneNumber.getNumber());
            }
        }
    }

    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.err.println("Usage: ListPeople ADDRESS_BOOK_FILE");
        }

        // Read the existing address book
        AddressBook addressBook = AddressBook.parseFrom(new FileInputStream(args[0]));

        Print(addressBook);
    }
}
