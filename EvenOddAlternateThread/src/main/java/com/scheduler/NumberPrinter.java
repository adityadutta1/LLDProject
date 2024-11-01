package com.scheduler;

class NumberPrinter {
    private int count = 1;
    private final int maxCount;
    private boolean isEven = false;

    public NumberPrinter(int maxCount) {
        this.maxCount = maxCount;
    }

    public synchronized void printEven() {
        while (count <= maxCount) {
            // Wait while it's not even's turn or number is not even
            while (!isEven && count <= maxCount) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }

            if (count <= maxCount) {
                System.out.println("Even Thread: " + count);
                count++;
                isEven = false;
                notifyAll();
            }
        }
    }

    public synchronized void printOdd() {
        while (count <= maxCount) {
            // Wait while it's even's turn or number is not odd
            while (isEven && count <= maxCount) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }

            if (count <= maxCount) {
                System.out.println("Odd Thread: " + count);
                count++;
                isEven = true;
                notifyAll();
            }
        }
    }
}
