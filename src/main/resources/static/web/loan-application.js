const app = Vue.

    createApp({
        data() {
            return {
                accounts: [],
                clients: [],
                loans: [],
                payments: [],

                mortgage: [],
                personal: [],
                automotive: [],

                loanId: "",
                loanName: "",
                amount: "",
                selectAccount: "",
                payment: "",

            }
        },

        created() {
            axios.get('/api/clients/current')
                .then((response) => {
                    this.clients = response.data;
                    this.accounts = this.clients.accounts.sort((a, b) => {
                        return a.id - b.id
                    });

                })
                .catch(function (error) {
                    console.log(error);
                });

            axios.get('/api/loans')
                .then((response) => {
                    this.loans = response.data;
                    this.mortgage = this.loans.filter(j => j.name == "Mortgage")[0].interest / 100;
                    this.personal = this.loans.filter(j => j.name == "Personal")[0].interest / 100;
                    this.automotive = this.loans.filter(j => j.name == "Automotive")[0].interest / 100;
                });

        },

        methods: {
            newDate(creationDate) {
                creationDate = new Date(creationDate).toLocaleString();

                return creationDate;
            },
            newBalance(balance) {
                balance = new Intl.NumberFormat('en-US', { style: 'currency', currency: 'USD' }).format(balance);
                return balance;
            },
            logOut() {
                axios.post('/api/logout')
                    .then(response => location.href = '/web/index.html')
            },
            newLoan() {
                axios.post("/api/clients/current/loans", {
                    "id": `${this.loanId}`,
                    "amount": `${this.amount}`,
                    "payments": `${this.payments}`,
                    "destinyAccount": `${this.selectAccount}`,
                })
                    .then(response => {
                        window.alert("Loan granted"), location.href = '/web/accounts.html';
                    })
                    .catch(error => {
                        window.alert("Loan denied");
                    });
            },
        },

    }).mount('#app')