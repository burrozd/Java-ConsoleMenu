import java.util.Arrays;
import java.util.Comparator;
import java.util.Scanner;

public class ConsoleMenu {
    private static int[] elpriser = new int[24];

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        char choice;

        do {
            displayMenu();
            System.out.print("Välj ett alternativ: ");
            choice = scanner.next().charAt(0);
            scanner.nextLine();

            switch (choice) {
                case '1':
                    inputPrices(scanner);
                    break;
                case '2':
                    calculateMinMaxAvg();
                    break;
                case '3':
                    sortPrices();
                    break;
                case '4':
                    bestChargeTime();
                    break;
                case 'e':
                case 'E':
                    System.out.println("Programmet avslutas.");
                    break;
                default:
                    System.out.println("Ogiltigt val. Försök igen.");
                    break;
            }
        } while (choice != 'e' && choice != 'E');

        scanner.close();
    }

    private static void displayMenu() {
        System.out.println("Elpriser");
        System.out.println("=======");
        System.out.println("1. Inmatning");
        System.out.println("2. Min, Max och Medel");
        System.out.println("3. Sortera");
        System.out.println("4. Bästa Laddningstid (4h)");
        System.out.println("e. Avsluta");
    }

    private static void inputPrices(Scanner scanner) {
        System.out.println("Ange elpriser för varje timme:");

        for (int i = 0; i < 24; i++) {
            int nextHour = (i + 1) % 24;
            String timeInterval = String.format("%02d-%02d", i, (nextHour == 0) ? 0 : nextHour);
            System.out.print(timeInterval + ": ");
            int price = scanner.nextInt();
            elpriser[i] = price;
        }

        System.out.println("Elpriser har sparats.");
    }

    private static void calculateMinMaxAvg() {
        int minPrice = elpriser[0];
        int maxPrice = elpriser[0];
        int sum = elpriser[0];

        for (int i = 1; i < 24; i++) {
            if (elpriser[i] < minPrice) {
                minPrice = elpriser[i];
            }
            if (elpriser[i] > maxPrice) {
                maxPrice = elpriser[i];
            }
            sum += elpriser[i];
        }

        double averagePrice = (double) sum / 24;

        System.out.println("Lägsta pris: " + minPrice);
        System.out.println("Högsta pris: " + maxPrice);
        System.out.println("Timmarna med lägsta pris:");

        for (int i = 0; i < 23; i++) {
            if (elpriser[i] == minPrice) {
                int nextHour = (i + 1) % 24;
                String timeInterval = String.format("%02d-%02d", i, nextHour);
                System.out.println(timeInterval);
            }
        }

        System.out.println("Timmarna med högsta pris:");

        for (int i = 0; i < 24; i++) {
            if (elpriser[i] == maxPrice) {
                int nextHour = (i + 1) % 24;
                String timeInterval = String.format("%02d-%02d", i, nextHour);
                System.out.println(timeInterval);
            }
        }

        System.out.println("Dygnets medelpris: " + averagePrice);
    }

    private static void sortPrices() {
        String[] timePricePairs = new String[24];

        for (int i = 0; i < 24; i++) {
            String timeInterval = String.format("%02d-%02d", i, (i + 1) % 24);
            timePricePairs[i] = timeInterval + " " + elpriser[i] + " öre";
        }

        Arrays.sort(timePricePairs, new Comparator<String>() {
            public int compare(String pair1, String pair2) {
                int price1 = Integer.parseInt(pair1.split(" ")[1]);
                int price2 = Integer.parseInt(pair2.split(" ")[1]);
                return Integer.compare(price1, price2);
            }
        });

        for (String pair : timePricePairs) {
            System.out.println(pair);
        }
    }


        private static void bestChargeTime() {
            int currentStartIndex = 0;
            int currentTotalPrice = 0;
            int bestStartIndex = 0;
            int bestTotalPrice = Integer.MAX_VALUE;
            int hourCounter = 1;

            for (int currentIndex = 0; currentIndex < 24; currentIndex++) {
                currentTotalPrice += elpriser[currentIndex];
                hourCounter++;

                if (hourCounter == 4) {
                    if (currentTotalPrice < bestTotalPrice) {
                        bestTotalPrice = currentTotalPrice;
                        bestStartIndex = currentStartIndex;
                    }

                    currentTotalPrice -= elpriser[currentStartIndex];
                    currentStartIndex++;
                    hourCounter = 1;
                }
                else if (hourCounter > 4) {
                    currentTotalPrice -= elpriser[currentStartIndex];
                    currentStartIndex++;
                    hourCounter--;
                }
            }

            // Beräkna genomsnittspriset för de bästa 4 timmarna
            double averagePrice = (double) bestTotalPrice / 4;

            System.out.println("Bästa laddningstid (4h) börjar vid: " + bestStartIndex);
            System.out.println("Genomsnittligt pris för de bästa 4 timmarna: " + averagePrice + " öre per kWh");
        }

    }
