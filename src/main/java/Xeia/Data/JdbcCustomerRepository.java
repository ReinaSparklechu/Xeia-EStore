package Xeia.Data;

import Xeia.Customer.Customer;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementCreatorFactory;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;


@Repository
public class JdbcCustomerRepository implements CustomerRepository {
    private JdbcTemplate jdbc;
    private Firestore db;
    @Autowired
    public JdbcCustomerRepository(JdbcTemplate jdbc) throws IOException, ExecutionException, InterruptedException {
        this.jdbc = jdbc;
        FirebaseApp.initializeApp(options);
        db = FirestoreClient.getFirestore();
        List< QueryDocumentSnapshot> documents = db.collection("Reddit").get().get().getDocuments();

    }

    // TODO: 19/8/2022 replace the repositories with firebase 
    @Override
    public Customer loginCustomer(String name, String passHash) {
        return null;
    }
    FileInputStream serviceAccount = new FileInputStream("src/main/resources/reina-s-base-firebase-adminsdk-y5kk5-e559e59132.json");
    FirebaseOptions options = new FirebaseOptions.Builder()
            .setCredentials(GoogleCredentials.fromStream(serviceAccount))
            .build();


    private String convertToHex(final byte[] messageDigest) {
        BigInteger bigint = new BigInteger(1, messageDigest);
        String hexText = bigint.toString(16);
        while (hexText.length() < 32) {
            hexText = "0".concat(hexText);
        }
        return hexText;
    }

    @Override
    public void signUpCustomer(Customer newCustomer) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(newCustomer.getPassword().getBytes(StandardCharsets.UTF_8));
        String passwordHash = convertToHex(md.digest());
        System.out.println("Bytes for password: " + newCustomer.getPassword() +" is: " + passwordHash);
        PreparedStatementCreatorFactory pscf = new PreparedStatementCreatorFactory("insert into Customer (username, passwordHash) values (?,?)", Types.VARCHAR,Types.VARCHAR);
        pscf.setReturnGeneratedKeys(true);
        PreparedStatementCreator psc = pscf.newPreparedStatementCreator(
                Arrays.asList(
                        newCustomer.getUsername()
                        , passwordHash));
        KeyHolder kh = new GeneratedKeyHolder();
        jdbc.update(psc,kh);
        jdbc.update("update Customer set userId = " + kh.getKey().longValue() + " where username = \'" + newCustomer.getUsername() + "\' and userId = null");
        newCustomer.setUserId(kh.getKey().longValue());
        System.out.println("Signup complete for: " + newCustomer);
    }
    private Customer mapRowToCustomer(ResultSet rs, int rowNum)throws SQLException {
        return new Customer(rs.getString("userName"), rs.getLong("userId"));
    }
}
