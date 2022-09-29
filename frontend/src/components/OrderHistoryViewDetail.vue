<template>

    <v-card outlined>
        <v-card-title>
            OrderHistory # {{$route.params.id }}
        </v-card-title>

        <v-card-text>
            <div>
                <Number label="OrderId" v-model="item.orderId" :editMode="editMode" @change="change" />
            </div>
            <div>
                <Number label="FlowerId" v-model="item.flowerId" :editMode="editMode" @change="change" />
            </div>
            <div>
                <String label="FlowerName" v-model="item.flowerName" :editMode="editMode" @change="change" />
            </div>
            <div>
                <Boolean label="IsOffline" v-model="item.isOffline" :editMode="editMode" @change="change" />
            </div>
            <div>
                <Boolean label="IsDelivered" v-model="item.isDelivered" :editMode="editMode" @change="change" />
            </div>
            <div>
                <Boolean label="IsCanceled" v-model="item.isCanceled" :editMode="editMode" @change="change" />
            </div>
            <div>
                <Number label="Qty" v-model="item.qty" :editMode="editMode" @change="change" />
            </div>
            <div>
                <Number label="Price" v-model="item.price" :editMode="editMode" @change="change" />
            </div>
        </v-card-text>
    </v-card>

</template>

<script>
  const axios = require('axios').default;

  export default {
    name: 'OrderHistoryViewDetail',
    props: {
      value: Object,
    },
    data: () => ({
        item : [],
    }),
    async created() {
      var params = this.$route.params;
      var temp = await axios.get(axios.fixUrl('/orderHistories/' + params.id))

      this.item = temp.data;

    },
    methods: {
    }
  }
</script>

