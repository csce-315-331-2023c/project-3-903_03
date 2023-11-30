<script>
    import { goto } from '$app/navigation';
    import { auth } from '$lib/auth.js';
    auth.set({
            isAuthenticated: true, 
            id : 7,
            username : 'guest',
            password : '',
            name : '',
            category : 'guest',
    });  
    goto(`/customer/place_order`, { replace: true });
</script>



