class CreatePosts < ActiveRecord::Migration[5.1]
  def up
    create_table :posts do |t|
      t.string :title
      t.string :subtitle
      t.text :content
      t.references :user, foreign_key: true

      t.timestamps
    end
  end

  def down
    drop_table :posts
  end
end
