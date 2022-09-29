<template>

    <v-data-table
        :headers="headers"
        :items="productList"
        :items-per-page="5"
        class="elevation-1"
    ></v-data-table>

</template>

<script>
    const axios = require('axios').default;

    export default {
        name: 'ProductListView',
        props: {
            value: Object,
            editMode: Boolean,
            isNew: Boolean
        },
        data: () => ({
            headers: [
                { text: "id", value: "id" },
            ],
            productList : [],
        }),
          async created() {
            var temp = await axios.get(axios.fixUrl('/productLists'))

            temp.data._embedded.productLists.map(obj => obj.id=obj._links.self.href.split("/")[obj._links.self.href.split("/").length - 1])

            this.productList = temp.data._embedded.productLists;
        },
        methods: {
        }
    }
</script>

