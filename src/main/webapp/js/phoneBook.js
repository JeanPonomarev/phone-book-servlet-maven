function Contact(firstName, lastName, phoneNumber) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.phoneNumber = phoneNumber;
    this.shown = true;
}

new Vue({
    el: "#app",

    data: {
        firstName: "",
        lastName: "",
        phoneNumber: "",
        contacts: [],
        selectedContacts: [],
        term: "",

        clientValidation: false,
        serverValidation: false,
        serverError: ""
    },

    methods: {
        contactToString: function (contact) {
            let note = "(";
            note += contact.firstName + ", ";
            note += contact.lastName + ", ";
            note += contact.phoneNumber;
            note += ")";

            return note;
        },

        convertContactList: function (contactListFromServer) {
            return contactListFromServer.map(function (contact, i) {
                return {
                    id: contact.id,
                    firstName: contact.firstName,
                    lastName: contact.lastName,
                    phoneNumber: contact.phoneNumber,
                    shown: true,
                    number: i + 1
                };
            });
        },

        loadData() {
            const self = this;

            const term = this.term;

            $.get("/api/getContacts", { term })
                .done(function (response) {
                const contactListFromServer = JSON.parse(response);
                self.contacts = self.convertContactList(contactListFromServer)
            }).fail(function () {
                alert("Failed to load contacts' list");
            });
        },

        addContact() {
            if (this.hasError) {
                this.clientValidation = true;
                this.serverValidation = false;

                return;
            }

            const self = this;

            const contact = new Contact(this.firstName, this.lastName, this.phoneNumber);

            $.ajax({
                type:"POST",
                url: "/api/addContact",
                data: JSON.stringify(contact)
            }).done(function () {
                self.serverValidation = false;
            }).fail(function (ajaxRequest) {
                const contactValidation = JSON.parse(ajaxRequest.responseText);
                self.serverError = contactValidation.error;
                self.serverValidation = true;
            }).always(function () {
                self.loadData();
            });

            self.firstName = "";
            self.lastName = "";
            self.phoneNumber = "";
            self.clientValidation = false;
        },

        deleteContact(contact) {
            const self = this;

            $.ajax({
                type:"POST",
                url: "/api/deleteContact",
                data: JSON.stringify(contact)
            }).done(function () {
                self.serverValidation = false;
            }).fail(function (ajaxRequest) {
                const contactValidation = JSON.parse(ajaxRequest.responseText);
                self.serverError = contactValidation.error;
                self.serverValidation = true;
            }).always(function () {
                self.loadData();
            });
        },

        deleteContactList() {
            if (this.selectedContacts.length === 0) {
                alert("No contacts selected");
                return;
            }

            const self = this;

            $.ajax({
                type:"POST",
                url: "/api/deleteContactList",
                data: JSON.stringify(self.selectedContacts)
            }).done(function () {
                self.serverValidation = false;
                self.selectedContacts = [];
            }).fail(function (ajaxRequest) {
                const contactValidation = JSON.parse(ajaxRequest.responseText);
                self.serverError = contactValidation.error;
                self.serverValidation = true;
            }).always(function () {
                self.loadData();
            });
        },

        resetFilter() {
            this.term = "";
            this.loadData();
        }
    },

    computed: {
        selectAll: {
            get: function () {
                if (this.contacts.length === 0) {
                    return false;
                }

                return this.contacts ? this.selectedContacts.length === this.contacts.length : false;
            },

            set: function (value) {
                let selectedContacts = [];

                if (value) {
                    this.contacts.forEach(function (contact) {
                        selectedContacts.push(contact);
                    })
                }

                this.selectedContacts = selectedContacts;
            }
        },

        firstNameError: function () {
            if (!this.firstName) {
                return {
                    message: "First name field should be filled",
                    error: true
                };
            }

            return {
                error: false
            };
        },

        lastNameError: function () {
            if (!this.lastName) {
                return {
                    message: "Last name field should be filled",
                    error: true
                };
            }

            return {
                error: false
            };
        },

        phoneNumberError: function () {
            if (!this.phoneNumber) {
                return {
                    message: "Phone number field should be filled",
                    error: true
                };
            }

            const self = this;

            const sameContact = this.contacts.some(function (contact) {
                return contact.phoneNumber === self.phoneNumber;
            });

            if (sameContact) {
                return {
                    message: "Phone number should not be duplicated",
                    error: true
                };
            }

            return {
                error: false
            };
        },

        hasError: function () {
            return this.firstNameError.error || this.lastNameError.error || this.phoneNumberError.error;
        }
    },

    created: function () {
        this.loadData();
    }
});