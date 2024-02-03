package ru.otus.moneybox;

public class MoneyBoxImpl implements MoneyBox {
    private int quantity;

    public int getQuantity() {
        return quantity;
    }

    @Override
    public void depositBanknotes(int quantity) {
        this.quantity += quantity;
    }

    @Override
    public void withdrawBanknotes(int quantity) {
        if (quantity > 0 && quantity <= this.quantity) {
            this.quantity -= quantity;
        }
    }
}
