const app = Vue.

    createApp({
        data() {
            return {
                accounts: [],
                loans: [],
                clients: [],
                interest: [],

            }
        },

        created() {
            axios.get('/api/clients/current')
                .then((response) => {
                    this.clients = response.data;
                    this.accounts = this.clients.accounts.sort((a, b) => {
                        return a.id - b.id
                    });
                    this.loans = this.clients.loans;
                    this.interest = this.loans.interest;

                    console.log(this.interest)

                    console.log(this.loans)

                })
                .catch(function (error) {
                    console.log(error);
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
            newAccount() {
                axios.post('/api/clients/current/accounts', { headers: { 'content-type': 'application/x-www-form-urlencoded' } })
                .then(response => {
                    window.alert("New account created successfully"), window.location.reload();
                })
                .catch(error => {
                    window.alert("You can't create a new account");
                });
            },

        }

    }).mount('#app')