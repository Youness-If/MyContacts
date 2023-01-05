public class MainActivity extends AppCompatActivity {

    private TextView textView;
    private Button btnDetailsContact, btnCall;
    private Uri uriContact;
    private static final int REQUEST_PICK_CONTACT = 1;
    private static final int Perm_CTC = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.textView);
        btnDetailsContact = findViewById(R.id.btn_details_contact);
        btnCall = findViewById(R.id.btn_call);

        btnDetailsContact.setEnabled(false);
        btnCall.setEnabled(false);

        Button btnContactID = findViewById(R.id.btn_contact_id);
        btnContactID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Demandez la permission READ_CONTACTS
                ActivityCompat.requestPermissions(MainActivity.this, new String[]
                        {Manifest.permission.READ_CONTACTS}, Perm_CTC);
                // Ouvrez l'application Contacts pour sélectionner un contact
                Intent pickContactIntent = new Intent(Intent.ACTION_PICK, Uri.parse("content://contacts/people/"));
                startActivityForResult(pickContactIntent, REQUEST_PICK_CONTACT);
            }
        });

        btnDetailsContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Interrogez la base de données Content provider contacts pour obtenir l'ID et le nom du contact
                Cursor cursor = getContentResolver().query(uriContact,
                        null, null, null, null);
                // Utilisez le curseur pour obtenir le nom du contact
                String contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                // Fermez le curseur
                cursor.close();
                // Interrogez à nouveau la base de données pour obtenir l'ID et le numéro de téléphone du contact
                Cursor cursorID = getContentResolver().query(uriContact,
                        null, null, null, null);
                // Utilisez le curseur pour obtenir l'ID du contact
                String contactID = cursorID.getString(cursorID.getColumnIndex(ContactsContract.Contacts._ID));
                // Fermez le curseur
                cursorID.close();
                // Interrogez à nouveau la base de données pour obtenir le numéro de téléphone du contact
                Cursor cursorPhone = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER},
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ? AND " +
                                ContactsContract.CommonDataKinds.Phone.TYPE + " = " +
                                ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE,
                        new String[]{contactID},
                        null);
                // Utilisez le curseur pour obtenir le numéro de téléphone
                String phoneNumber = cursorPhone.getString(cursorPhone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                // Fermez le curseur
                cursorPhone.close();
                // Affichez le nom et le numéro de téléphone du contact dans le TextView
                textView.setText("Nom : " + contactName + "\nNuméro : " + phoneNumber);
            }
        });

        btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Interrogez la base de données Content provider contacts pour obtenir l'ID et le numéro de téléphone du contact
                Cursor cursorPhone = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER},
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ? AND " +
                                ContactsContract.CommonDataKinds.Phone.TYPE + " = " +
                                ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE,
                        new String[]{
