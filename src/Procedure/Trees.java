package Procedure;

public interface Trees {
    // Interfaccia con metodi condivisi da tutti gli alberi
    boolean insert(int key, String value);
    Node search(int key);
    void reset();
}
