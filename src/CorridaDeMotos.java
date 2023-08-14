import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class CorridaDeMotos {
    private static final int NUM_COMPETIDORES = 10;
    private static final int NUM_CORRIDAS = 10;
    private static final Object lock = new Object();

    private static int corridaCount = 0;

    private static class Competidor implements Runnable {
        private final int id;
        private int pontos;

        public Competidor(int id) {
            this.id = id;
            this.pontos = 0;
        }

        public int getId() {
            return id;
        }

        public int getPontos() {
            return pontos;
        }

        @Override
        public void run() {
            for (int i = 0; i < NUM_CORRIDAS; i++) {
                corridaCount++;
                System.out.println("Competidor #" + id + " iniciou a corrida #" + corridaCount);
                try {
                    Thread.sleep((long) (Math.random() * 500));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                synchronized (lock) {
                    pontos += (NUM_COMPETIDORES - corridaCount + 1);
                    System.out.println("Competidor #" + id + " finalizou a corrida #" + corridaCount);
                    if (corridaCount == NUM_COMPETIDORES) {
                        corridaCount = 0;
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        List<Thread> threads = new ArrayList<>();
        List<Competidor> competidores = new ArrayList<>();

        for (int i = 0; i < NUM_COMPETIDORES; i++) {
            Competidor competidor = new Competidor(i + 1);
            competidores.add(competidor);
            Thread thread = new Thread(competidor, "Competidor #" + (i + 1));
            threads.add(thread);
            thread.start();
        }

        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        competidores.sort(Comparator.comparingInt(Competidor::getPontos).reversed());

        System.out.println("======= Podio =======");
        for (int i = 0; i <= 2; i++) {
            System.out.println("Competidor #" + competidores.get(i).getId() + " com " + competidores.get(i).getPontos() + " pontos");
        }
        System.out.println("======= Pontuação total =======");
        for (Competidor competidore : competidores) {
            System.out.println("Competidor #" + competidore.getId() + " com " + competidore.getPontos() + " pontos");
        }
    }
}
