class User < ActiveRecord::Base
  has_many  :posts, dependent: :destroy
  has_many  :comments, dependent: :destroy

  validates :title, presence: true
  validates :email, presence: true
end