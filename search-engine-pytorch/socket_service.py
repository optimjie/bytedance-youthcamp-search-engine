from socket import *
import json
import random
from sentence_transformers import SentenceTransformer, util
from PIL import Image
from PIL import ImageFile
import torch
import pickle
ImageFile.LOAD_TRUNCATED_IMAGES = True
HOST = '127.0.0.1'
PORT = 50000
BUFSIZ = 1024
ADDR = (HOST,PORT)
tcpServerSock = socket(AF_INET,SOCK_STREAM)
tcpServerSock.bind(ADDR)
tcpServerSock.listen(5)



torch.set_num_threads(4)

# 加载预训练模型
model = SentenceTransformer('clip-ViT-B-32')
text_model = SentenceTransformer('clip-ViT-B-32-multilingual-v1')

'''
加载图片embedding文件
使用了wukong数据集前000~051中的前2w张图片 
百度网盘下载地址

链接：https://pan.baidu.com/s/1i8KANXjF0b-zDU2sRNbC8w 
提取码：1057 
--来自百度网盘超级会员V6的分享

'''
emb_filename = 'wukong_1m_embeddings.pkl'


# 读取pkl文件
with open(emb_filename, 'rb') as fIn:
    total_urls,total_emb = pickle.load(fIn)
img_emb = total_emb
img_urls = total_urls
print(len(img_urls))
print(img_emb.shape)


def search(query, k=8):
    # First, we encode the query (which can either be an image or a text string)
    query_emb = text_model.encode([query], convert_to_tensor=True, show_progress_bar=False)

    # Then, we use the util.semantic_search function, which computes the cosine-similarity
    # between the query embedding and all image embeddings.
    # It then returns the top_k highest ranked images, which we output
    hits = util.semantic_search(query_emb, img_emb, top_k=k)[0]

    print("Query:")
    # display(query)
    for hit in hits:
        url = img_urls[hit['corpus_id']]
        print(hit['corpus_id'])
        print(img_urls[hit['corpus_id']])
        url_list.append(url)


def search_img(img_file,k=8):
    input_img_emb = model.encode(Image.open(img_file) , convert_to_tensor=True, show_progress_bar=False)
    hits = util.semantic_search(input_img_emb, img_emb, top_k=k)[0]
    print("Query:")
    # display(query)
    for hit in hits:
        url = img_urls[hit['corpus_id']]
        print(img_urls[hit['corpus_id']])
        url_list.append(url)


while True:
    url_list = []
    print('等待连接')
    tcpClientSock, addr = tcpServerSock.accept()
    print('接收到来自 :', addr,' 的连接')
    while True:
        request_data = tcpClientSock.recv(BUFSIZ)
        if not request_data:
            break
        request_data_str = str(request_data, 'UTF-8')
        print("来自客户端的数据:" +request_data_str)
        # 字符串转换成对象
        data = json.loads(request_data_str)
        response_data = {}
        response_data['code'] = data['code']
        response_data['status'] = 20
        # 文字搜图服务
        if data['code']=='100':
            # 数据处理
            search_query = data['sentence']
            search(search_query)
            response_data['status'] = 10
            for i in range(url_list.__len__()):
                response_data['url'+str(i)] = url_list[i]
        # 以图搜图服务
        elif data['code']=='200':
            if random.randint(0, 100) < 100:
                img_path = data['filepath']
                search_img(img_path)
                response_data['status'] = 10
                for i in range(url_list.__len__()):
                    response_data['url' + str(i)] = url_list[i]
            else:
                response_data['status'] = 0
        # 无对应服务
        else:
            response_data['code'] = '000'
            response_data['status'] = 10

        # 对象转换成字符串
        response_data_str=json.dumps(response_data)
    tcpClientSock.send(bytes(response_data_str, encoding = "utf8"))
    tcpClientSock.close()
    url_list.clear()
tcpServerSock.close()

