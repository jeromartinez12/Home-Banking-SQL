const urlParams = new URLSearchParams(window.location.search);
const urlName = urlParams.get('id');

const app = Vue.
createApp({
    data(){
        return {
            clients: [],
            accounts: [],
            idAccounts: [],
            transactions: [],

        }
    },

    created(){
        axios.get('/api/clients/current')
        .then((response) => {
            this.clients = response.data;
            this.accounts = this.clients.accounts.sort((a, b)=> {
                return a.id - b.id
            });
            this.idAccounts = this.accounts.find(account => account.id == urlName);
            this.transactions = this.idAccounts.transaction.sort((a,b) =>{
                return a.id - b.id});

        })
        .catch(function (error) {
            console.log(error);
        });
    },

    methods: {
        newDate(date){
            creationDate = new Date(date).toLocaleString();

            return creationDate;
        },

        newBalance(balance){
            balance = new Intl.NumberFormat('en-US', { style: 'currency', currency: 'USD' }).format(balance);
            return balance;
        },
        logOut() {
            axios.post('/api/logout')
                .then(response => location.href = '/web/index.html')
        },
    }

}).mount('#app')