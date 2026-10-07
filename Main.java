import java.io.*;
import java.net.*;
import java.util.*;

public class Main {

    static final int PORT = 5000;

    // 0 = puste
    // 1 = statek
    // 2 = trafiony statek
    static int[] mojaPlansza = new int[100];

    // Plansza przeciwnika:
    // 0 = nieznane
    // 1 = pudło
    // 2 = trafienie
    static int[] planszaPrzeciwnika = new int[100];

    static BufferedReader in;
    static PrintWriter out;
    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {

        System.out.println("=== GRA W STATKI ===");
        System.out.println("1 - HOST");
        System.out.println("2 - CLIENT");
        System.out.print("Wybierz: ");

        int wybor = scanner.nextInt();

        // Przykładowe statki
        ustawStatki();

        try {

            if (wybor == 1) {
                uruchomHost();
            } else if (wybor == 2) {
                uruchomClient();
            } else {
                System.out.println("Nieprawidłowy wybór.");
            }

        } catch (IOException e) {
            System.out.println("Błąd połączenia:");
            e.printStackTrace();
        }
    }



    static void uruchomHost() throws IOException {

        ServerSocket serverSocket = new ServerSocket(PORT);

        System.out.println();
        System.out.println("HOST");
        System.out.println("Czekam na przeciwnika...");

        Socket socket = serverSocket.accept();

        System.out.println("Przeciwnik połączony!");

        ustawPolaczenie(socket);

        // Host zaczyna
        gra(true);

        socket.close();
        serverSocket.close();
    }


    static void uruchomClient() throws IOException {

        scanner.nextLine();

        System.out.print("Podaj IP hosta: ");
        String ip = scanner.nextLine();

        System.out.println("Łączenie z hostem...");

        Socket socket = new Socket(ip, PORT);

        System.out.println("Połączono z hostem!");

        ustawPolaczenie(socket);

        // Client zaczyna po otrzymaniu pierwszego ruchu
        gra(false);

        socket.close();
    }



    static void ustawPolaczenie(Socket socket) throws IOException {

        in = new BufferedReader(
                new InputStreamReader(socket.getInputStream())
        );

        out = new PrintWriter(
                socket.getOutputStream(),
                true
        );
    }



    static void gra(boolean zaczynam) throws IOException {

        boolean mojaTura = zaczynam;

        while (true) {

            if (mojaTura) {



                int pole = wybierzPole();

                out.println("STRZAL " + pole);

                // Czekamy na wynik
                String odpowiedz = in.readLine();

                if (odpowiedz == null) {
                    System.out.println("Przeciwnik rozłączył się.");
                    return;
                }

                System.out.println(odpowiedz);

                String[] dane = odpowiedz.split(" ");

                if (!dane[0].equals("WYNIK")) {
                    System.out.println("Nieprawidłowa odpowiedź.");
                    return;
                }

                int wynik = Integer.parseInt(dane[1]);

                if (wynik == 0) {
                    System.out.println("PUDŁO!");
                    planszaPrzeciwnika[pole] = 1;

                    mojaTura = false;

                } else if (wynik == 1) {
                    System.out.println("TRAFIENIE!");
                    planszaPrzeciwnika[pole] = 2;

                    mojaTura = false;

                } else if (wynik == 2) {
                    System.out.println("ZATOPIONY!");
                    planszaPrzeciwnika[pole] = 2;

                    mojaTura = false;

                } else if (wynik == 3) {
                    System.out.println("WYGRAŁEŚ!");
                    return;
                }

            } else {



                System.out.println();
                System.out.println("Czekam na strzał przeciwnika...");

                String komunikat = in.readLine();

                if (komunikat == null) {
                    System.out.println("Przeciwnik rozłączył się.");
                    return;
                }

                System.out.println("Otrzymano: " + komunikat);

                String[] dane = komunikat.split(" ");

                if (!dane[0].equals("STRZAL")) {
                    System.out.println("Nieprawidłowy komunikat.");
                    return;
                }

                int pole = Integer.parseInt(dane[1]);

                // Sprawdzamy strzał
                int wynik;

                if (mojaPlansza[pole] == 1) {

                    mojaPlansza[pole] = 2;

                    System.out.println(
                            "Przeciwnik trafił pole " + pole
                    );

                    if (czyWszystkieStatkiZatopione()) {

                        wynik = 3;

                    } else {

                        wynik = 1;
                    }

                } else {

                    System.out.println(
                            "Przeciwnik chybił pole " + pole
                    );

                    wynik = 0;
                }

                // Odsyłamy wynik
                out.println("WYNIK " + wynik);

                if (wynik == 3) {

                    System.out.println();
                    System.out.println("PRZEGRAŁEŚ!");
                    return;
                }

                // Teraz kolej przeciwnika
                mojaTura = true;
            }
        }
    }


    static int wybierzPole() {

        while (true) {

            System.out.print("STRZAL ");

            int pole = scanner.nextInt();

            if (pole < 0 || pole > 99) {

                System.out.println(
                        "Pole musi być w zakresie 0-99."
                );

                continue;
            }

            // Nie można strzelać drugi raz
            if (planszaPrzeciwnika[pole] != 0) {

                System.out.println(
                        "Już strzelałeś w to pole."
                );

                continue;
            }

            return pole;
        }
    }

    // =========================
    // STATKI
    // =========================

    static void ustawStatki() {

        // Przykładowy statek 3-polowy
        mojaPlansza[10] = 1;
        mojaPlansza[11] = 1;
        mojaPlansza[12] = 1;

        // Statek 2-polowy
        mojaPlansza[25] = 1;
        mojaPlansza[26] = 1;

        // Statek 3-polowy
        mojaPlansza[50] = 1;
        mojaPlansza[60] = 1;
        mojaPlansza[70] = 1;
    }



    static boolean czyWszystkieStatkiZatopione() {

        for (int i = 0; i < 100; i++) {

            if (mojaPlansza[i] == 1) {
                return false;
            }
        }

        return true;
    }
}