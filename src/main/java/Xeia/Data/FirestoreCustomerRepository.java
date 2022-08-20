package Xeia.Data;

import Xeia.Customer.Customer;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.errorprone.annotations.concurrent.LazyInit;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Repository;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.List;


public class FirestoreCustomerRepository implements CustomerRepository{
    FileInputStream serviceAccount = new FileInputStream("src/main/resources/reina-s-base-firebase-adminsdk-y5kk5-e559e59132.json");
    FirebaseOptions options = new FirebaseOptions.Builder()
            .setCredentials(GoogleCredentials.fromStream(serviceAccount))
            .build();
    Firestore db;

    public FirestoreCustomerRepository() throws IOException {
        FirebaseApp.initializeApp(options);
        db = FirestoreClient.getFirestore();

    }

    @Override
    public Customer loginCustomer(String name, String passHash) {
        db.collection("customer").document("name");
        return null;
    }

    @Override
    public void signUpCustomer(Customer newCustomer) throws NoSuchAlgorithmException {

    }
}
