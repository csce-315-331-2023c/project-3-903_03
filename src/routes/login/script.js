let username = '';
let password = '';

async function handleLogin() {

const response = await fetch('/api/posts', {
  method: 'POST',
  headers: {
    'Content-Type': 'application/json',
  },
  body: JSON.stringify({ username, password }),
});

  if (response.status === 200) {
    print("success");
  } else {
    print("error");
  }
}