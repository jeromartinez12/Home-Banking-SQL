const app = Vue.

    createApp({
        data() {
            return {
                accounts: [],
                loans: [],
                clients: [],
                cards: [],
                welcome: "",
            }
        },

        created() {
            axios.get('/api/clients/current')
                .then((response) => {
                    this.clients = response.data;
                    this.accounts = this.clients.accounts.sort((a, b)=> {
                        return a.id - b.id
                    });
                    this.loans = this.clients.loans;
                    this.cards = this.clients.cards;

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
                    .then(response => window.location.reload())
                    .catch(console.log(error))
            },
            currentDateTime() {
                const time = new Date().getHours();

                if (time >= 6 && time < 12) {
                    return this.welcome = "Good Morning";
                }
                else if (time >= 12 && time < 18) {
                    return this.welcome = "Good Afternoon";
                }
                else if (time >= 18 && time < 22) {
                    return this.welcome = "Good Evening";
                } else {
                    return this.welcome = "Good Night";
                }
            },
        }

    }).mount('#app')





