const images = [
    {id:1, name:"Chocolate Chip Cookie.jpeg"},
    {id:2, name:"Snickerdoodle Cookie.jpeg"},  
    {id:3, name:"Sugar Cookie with M&Ms Cookie.jpeg"},  
    {id:4, name:"Double Chocolate Chip Cookie.png"},  
    {id:5, name:"Pecan Chocolate Chip Cookie.jpeg"},  
    {id:6, name:"Peanut Butter Cookie.webp"},  
    {id:7, name:"Sugar Cookie.webp"},  
    {id:8, name:"Oatmeal Raisin Cookie.jpeg"},  
    {id:9, name:"Peanut Butter Chocolate Chip Cookie.jpeg"},  
    {id:10, name:"Connor Man Brookie.png"},  
    {id:11, name:"Brownie.jpeg"},  
    {id:12, name:"Peanut Butter Chocolate Bar.jpeg"},  
    {id:13, name:"Salted Caramel Blondie.jpeg"},  
    {id:14, name:"Oatmeal Chocolate Chip Cookie.jpeg"},  
    {id:15, name:"Vanilla Ice Cream Pint.webp"},  
    {id:16, name:"Mint Chocolate Chip Pint.webp"},  
    {id:17, name:"Chocolate Ice Cream Pint.jpeg"},  
    {id:18, name:"Cookies n Cream Pint.jpeg"},  
    {id:19, name:"Milk 1p.png"},  
    {id:20, name:"Chocolate Milk.png"},  
    {id:21, name:"Gingerbread Cookie.jpeg"},  
  ];

export function get_image(id) {
    const image = images.find(i => i.id === id);
    if (image)
        return `/pictures/${image.name}`;
    else
        return ``;
}
