export async function createUser(

    username: string,
    password: string
  ): Promise<void> {
    const sql = `
    insert into _user (name, username, password, category_id)
    values ($name, $username, $password, 'admin:moderator')
  `;
  
    const hashedPassword = await bcrypt.hash(password, 12);
  
    const stmnt = db.prepare(sql);
    stmnt.run({ username, password: hashedPassword });
  }
  