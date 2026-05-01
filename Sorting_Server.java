import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

import java.io.*;
import java.net.InetSocketAddress;
import java.util.*;

public class Sorting_Server {

    public static void main(String[] args) throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
        server.createContext("/sort", new SortHandler());
        server.setExecutor(null);
        server.start();

        System.out.println("Server running at http://localhost:8000");
    }

    static class SortHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {   //exception handling

            exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");

            if (!exchange.getRequestMethod().equalsIgnoreCase("POST")) {
                exchange.sendResponseHeaders(405, -1);
                return;
            }

            BufferedReader br = new BufferedReader(
                    new InputStreamReader(exchange.getRequestBody())
            );

            // Format: numbers|algorithm
            String input = br.readLine();
            String[] parts = input.split("\\|");

            int[] arr = Arrays.stream(parts[0].split(","))
                    .mapToInt(Integer::parseInt)
                    .toArray();

            String algo = parts.length > 1 ? parts[1] : "bubble";

            switch (algo) {
                case "bubble":
                    bubbleSort(arr);
                    break;
                case "selection":
                    selectionSort(arr);
                    break;
                case "insertion":
                    insertionSort(arr);
                    break;
                case "merge":
                    mergeSort(arr, 0, arr.length - 1);
                    break;
                case "quick":
                    quickSort(arr, 0, arr.length - 1);
                    break;
                default:
                    bubbleSort(arr);
            }

            String response = Arrays.toString(arr);

            exchange.sendResponseHeaders(200, response.length());
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }

        // 🔹 Bubble Sort
        void bubbleSort(int[] arr) {
            for (int i = 0; i < arr.length - 1; i++) {
                for (int j = 0; j < arr.length - i - 1; j++) {
                    if (arr[j] > arr[j + 1]) {
                        int temp = arr[j];
                        arr[j] = arr[j + 1];
                        arr[j + 1] = temp;
                    }
                }
            }
        }

        // 🔹 Selection Sort
        void selectionSort(int[] arr) {
            for (int i = 0; i < arr.length; i++) {
                int min = i;
                for (int j = i + 1; j < arr.length; j++) {
                    if (arr[j] < arr[min]) min = j;
                }
                int temp = arr[i];
                arr[i] = arr[min];
                arr[min] = temp;
            }
        }

        // 🔹 Insertion Sort
        void insertionSort(int[] arr) {
            for (int i = 1; i < arr.length; i++) {
                int key = arr[i];
                int j = i - 1;
                while (j >= 0 && arr[j] > key) {
                    arr[j + 1] = arr[j];
                    j--;
                }
                arr[j + 1] = key;
            }
        }

        // 🔹 Merge Sort
        void mergeSort(int[] arr, int l, int r) {
            if (l < r) {
                int m = (l + r) / 2;
                mergeSort(arr, l, m);
                mergeSort(arr, m + 1, r);
                merge(arr, l, m, r);
            }
        }

        void merge(int[] arr, int l, int m, int r) {
            int[] left = Arrays.copyOfRange(arr, l, m + 1);
            int[] right = Arrays.copyOfRange(arr, m + 1, r + 1);

            int i = 0, j = 0, k = l;

            while (i < left.length && j < right.length) {
                if (left[i] <= right[j]) arr[k++] = left[i++];
                else arr[k++] = right[j++];
            }

            while (i < left.length) arr[k++] = left[i++];
            while (j < right.length) arr[k++] = right[j++];
        }

        // 🔹 Quick Sort
        void quickSort(int[] arr, int low, int high) {
            if (low < high) {
                int pi = partition(arr, low, high);
                quickSort(arr, low, pi - 1);
                quickSort(arr, pi + 1, high);
            }
        }

        int partition(int[] arr, int low, int high) {
            int pivot = arr[high];
            int i = low - 1;

            for (int j = low; j < high; j++) {
                if (arr[j] < pivot) {
                    i++;
                    int temp = arr[i];
                    arr[i] = arr[j];
                    arr[j] = temp;
                }
            }

            int temp = arr[i + 1];
            arr[i + 1] = arr[high];
            arr[high] = temp;

            return i + 1;
        }
    }
}
