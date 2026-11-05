// Sign-up looks up these two roles, so they must exist before the first account is created.
const app = db.getSiblingDB("pokerplanning");

for (const name of ["DEVELOPPER", "PRODUCTOWNER"]) {
  app.posts.updateOne({ name }, { $setOnInsert: { name } }, { upsert: true });
}
