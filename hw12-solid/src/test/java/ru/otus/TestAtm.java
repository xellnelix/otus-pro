package ru.otus;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.otus.atm.*;
import ru.otus.denomination.Denomination;
import ru.otus.exception.*;
import ru.otus.moneybox.MoneyBoxStorage;

class TestAtm {
    private AtmImpl atm;

    @BeforeEach
    void createAtm() {
        atm = new AtmImpl(new MoneyBoxStorage());
    }

    @Test
    void testDepositNullDenomination() {
        assertThrows(NullPointerException.class, () -> atm.depositMoney(null, 10));
    }

    @Test
    void testDepositCorrectValue() throws IncorrectBanknotesQuantityException {
        atm.depositMoney(Denomination.ONE_THOUSAND, 10);
        int expectedBalance = 10000;
        assertEquals(atm.showBalance(), expectedBalance);
    }

    @Test
    void testDepositIncorrectBanknotesQuantity() {
        assertThrows(IncorrectBanknotesQuantityException.class, () -> atm.depositMoney(Denomination.ONE_THOUSAND, -5));
    }

    @Test
    void testWithdrawBanknotesCollectException() throws IncorrectBanknotesQuantityException {
        atm.depositMoney(Denomination.FIVE_HUNDREDS, 10);
        atm.depositMoney(Denomination.FIVE_THOUSANDS, 5);
        Exception exception = assertThrows(WithdrawBanknotesCollectException.class, () -> atm.withdrawMoney(5400));
        String expectedMessage = "Error: Can't withdraw";
        String actualMessage = exception.getMessage();
        int expectedBalance = 30000;
        assertEquals(actualMessage, expectedMessage);
        assertEquals(atm.showBalance(), expectedBalance);
    }

    @Test
    void testIncorrectSumWithdrawException() {
        assertThrows(IncorrectSumWithdrawException.class, () -> atm.withdrawMoney(-100));
    }

    @Test
    void testWithdrawCorrect()
            throws IncorrectBanknotesQuantityException, WithdrawBanknotesCollectException,
                    IncorrectSumWithdrawException {
        atm.depositMoney(Denomination.TWO_THOUSANDS, 10);
        atm.withdrawMoney(6000);
        int expectedBalance = 14000;
        assertEquals(atm.showBalance(), expectedBalance);
    }
}
