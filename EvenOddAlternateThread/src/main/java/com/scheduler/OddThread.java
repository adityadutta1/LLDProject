package com.scheduler;


class OddThread extends Thread {
    private final NumberPrinter printer;

    public OddThread(NumberPrinter printer) {
        this.printer = printer;
    }

    @Override
    public void run() {
        printer.printOdd();
    }
}
