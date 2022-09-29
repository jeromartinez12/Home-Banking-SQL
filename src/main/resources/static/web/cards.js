const app = Vue.
    createApp({
        data() {
            return {
                clients: [],
                cards: [],
                creditCards: [],
                debitCards: [],

                

                cardType: '',
                thruDate: '',
                cardColor: '',
                cardType: '',
            }
        },

        created() {
            axios.get('/api/clients/current')
                .then(response => {
                    this.clients = response.data;
                    this.cards = this.clients.cards;

                    this.creditCards = this.cards.filter((credit) => {
                        return credit.type == "CREDIT"
                    })
                    this.debitCards = this.cards.filter((debit) => {
                        return debit.type == "DEBIT"
                    })

                })
                .catch(function (error) {
                    console.log(error);
                });
        },

        methods: {
            newDate(date) {
                creationDate = new Date(date).toLocaleString();
                return creationDate;
            },

            logOut() {
                axios.post('/api/logout')
                    .then(response => location.href = '/web/index.html')
            },
        },

        }).mount('#app')