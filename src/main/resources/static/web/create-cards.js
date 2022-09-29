const app = Vue.

    createApp({
        data() {
            return {
                cardType: [],
                cardColor: [],
                clients: [],
                cards: [],
                creditCards: [],
                debitCards: [],
            }
        },

        created() {
            axios.get('/api/clients/current')
                .then((response) => {
                    this.clients = response.data;
                    this.cards = this.clients.cards;
                    this.creditCards = this.cards.filter((credit)=>{
                        return credit.type == "CREDIT"
                    })
                    this.debitCards = this.cards.filter((debit)=>{
                        return debit.type == "DEBIT"
                    })
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
            newCard() {
                axios.post("/api/clients/current/cards", `cardType=${this.cardType}&cardColor=${this.cardColor}`,
                    { headers: { 'content-type': 'application/x-www-form-urlencoded' } })
                    .then(response => {
                        window.alert("New card created successfully"), location.href = '/web/cards.html';
                    })
                    .catch(error => {
                        window.alert("You can't create a new card");
                    });
            }
        }

    }).mount('#app')