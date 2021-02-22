// Конструктор
function Contact(firstName, lastName, phoneNumber) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.phoneNumber = phoneNumber;
    this.checked = false;
    this.shown = true;
}

new Vue({
    // привязываем компонент к DOM-элементу
    el: "#app",

    // данные компонента: контакт и валидация
    data: {
        firstName: "",
        lastName: "",
        phoneNumber: "",
        contacts: [],

        clientValidation: false,
        serverValidation: false,
        serverError: ""
    },

    methods: {
        // Конвертация js объекта contact в String
        contactToString: function (contact) {
            let note = "(";
            note += contact.firstName + ", ";
            note += contact.lastName + ", ";
            note += contact.phoneNumber;
            note += ")";

            return note;
        },

        // Конвертация серверного объекта contact в клиентский объект contact
        convertContactList: function (contactListFromServer) {
            return contactListFromServer.map(function (contact, i) {
                return {
                    id: contact.id,
                    firstName: contact.firstName,
                    lastName: contact.lastName,
                    phoneNumber: contact.phoneNumber,
                    checked: false,
                    shown: true,
                    number: i + 1
                };
            });
        },

        // Заграузка и парсинг данных с сервера
        loadData: function () {
            const self = this;

            $.get("/api/getContacts").done(function (response) {
                const contactListFromServer = JSON.parse(response);
                self.contacts = self.convertContactList(contactListFromServer)
            });
        },

        // Добавление контакта
        addContact: function () {

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

        deleteContact: function () {

        }
    },

    computed: {
        firstNameError: function () {
            if (!this.firstName) {
                return {
                    message: "First name field should be filled",
                    error: true
                };
            }

            return {
                message: "Success",
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
                message: "Success",
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
                message: "Success",
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