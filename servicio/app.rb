require 'sinatra'
require 'sinatra/cross_origin'
require 'sinatra/activerecord'
require 'json'
require './config/environments'
require './config/cors'
require './models/comment'
require './models/post'
require './models/user'

before do
  content_type :json
end

get '/posts' do
  Post.all.to_json(include: :user)
end

get '/posts/:id' do
  Post.where(id: params['id']).first.to_json(include: :comments)
end

post '/posts' do
  post = Post.new(params)

  if post.save
    post.to_json(include: :comments)
  else
    halt 422, post.errors.full_messages.to_json
  end
end

put '/posts/:id' do
  post = Post.where(id: params['id']).first

  if post
    post.name = params['name'] if params.has_key?('name')

    if post.save
      post.to_json
    else
      halt 422, post.errors.full_messages.to_json
    end
  end
end

delete '/posts/:id' do
  post = Post.where(id: params['id'])

  if post.destroy_all
    {success: "ok"}.to_json
  else
    halt 500
  end
end