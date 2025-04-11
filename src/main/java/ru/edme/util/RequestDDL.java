package ru.edme.util;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Repository
@Transactional
@RequiredArgsConstructor
public class RequestDDL {

    @PersistenceContext
    private final EntityManager entityManager;

    public void clearTables() {
        String clearTablesPath = "src/main/resources/db/changelog/ddl/clear_tables.sql";
        String clearTablesSql;
        try {
            clearTablesSql = new String(Files.readAllBytes(Paths.get(clearTablesPath)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        entityManager.createNativeQuery(clearTablesSql).executeUpdate();
    }

    public void deleteTables() {
        String dropAllTablesPath = "src/main/resources/db/changelog/ddl/drop_all_tables.sql";
        String dropAllTablesSql;
        try {
            dropAllTablesSql = new String(Files.readAllBytes(Paths.get(dropAllTablesPath)));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        entityManager.createNativeQuery(dropAllTablesSql).executeUpdate();
    }

    public void creatTables() {
        try {
            // CardStatus
            String createCardStatusPath = "src/main/resources/db/changelog/ddl/create_card_status.sql";
            String cardStatusSql = new String(Files.readAllBytes(Paths.get(createCardStatusPath)));
            entityManager.createNativeQuery(cardStatusSql).executeUpdate();

            // create_payment_system
            String createPaymentSystemPath = "src/main/resources/db/changelog/ddl/create_payment_system.sql";
            String createPaymentSystemSql = new String(Files.readAllBytes(Paths.get(createPaymentSystemPath)));
            entityManager.createNativeQuery(createPaymentSystemSql).executeUpdate();

            // create_issuing_bank
            String createIssuingBankPath = "src/main/resources/db/changelog/ddl/create_issuing_bank.sql";
            String createIssuingBankSql = new String(Files.readAllBytes(Paths.get(createIssuingBankPath)));
            entityManager.createNativeQuery(createIssuingBankSql).executeUpdate();

            // create_currency
            String createCurrencyPath = "src/main/resources/db/changelog/ddl/create_currency.sql";
            String createCurrencySql = new String(Files.readAllBytes(Paths.get(createCurrencyPath)));
            entityManager.createNativeQuery(createCurrencySql).executeUpdate();

            // Account
            String createAccountPath = "src/main/resources/db/changelog/ddl/create_account.sql";
            String createAccountSql = new String(Files.readAllBytes(Paths.get(createAccountPath)));
            entityManager.createNativeQuery(createAccountSql).executeUpdate();

            // Card
            String createCardPath = "src/main/resources/db/changelog/ddl/create_card.sql";
            String createCardSql = new String(Files.readAllBytes(Paths.get(createCardPath)));
            entityManager.createNativeQuery(createCardSql).executeUpdate();

            // create_response_code
            String createResponseCodePath = "src/main/resources/db/changelog/ddl/create_response_code.sql";
            String createResponseCodeSql = new String(Files.readAllBytes(Paths.get(createResponseCodePath)));
            entityManager.createNativeQuery(createResponseCodeSql).executeUpdate();

            // create_transaction_type
            String createTransactionTypePath = "src/main/resources/db/changelog/ddl/create_transaction_type.sql";
            String createTransactionTypeSql = new String(Files.readAllBytes(Paths.get(createTransactionTypePath)));
            entityManager.createNativeQuery(createTransactionTypeSql).executeUpdate();

            // create_merchant_category_code
            String createMerchantCategoryCodePath = "src/main/resources/db/changelog/ddl/create_merchant_category_code.sql";
            String createMerchantCategoryCodeSql = new String(Files.readAllBytes(Paths.get(createMerchantCategoryCodePath)));
            entityManager.createNativeQuery(createMerchantCategoryCodeSql).executeUpdate();

            // create_acquiring_bank
            String createAcquiringBankPath = "src/main/resources/db/changelog/ddl/create_acquiring_bank.sql";
            String createAcquiringBankSQL = new String(Files.readAllBytes(Paths.get(createAcquiringBankPath)));
            entityManager.createNativeQuery(createAcquiringBankSQL).executeUpdate();

            // create_sales_point
            String createSalesPointPath = "src/main/resources/db/changelog/ddl/create_sales_point.sql";
            String createSalesPointSQL = new String(Files.readAllBytes(Paths.get(createSalesPointPath)));
            entityManager.createNativeQuery(createSalesPointSQL).executeUpdate();

            // create_terminal
            String createTerminalPath = "src/main/resources/db/changelog/ddl/create_terminal.sql";
            String createTerminalSQL = new String(Files.readAllBytes(Paths.get(createTerminalPath)));
            entityManager.createNativeQuery(createTerminalSQL).executeUpdate();

            // create_transaction
            String createTransactionPath = "src/main/resources/db/changelog/ddl/create_transaction.sql";
            String createTransactionSQL = new String(Files.readAllBytes(Paths.get(createTransactionPath)));
            entityManager.createNativeQuery(createTransactionSQL).executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
