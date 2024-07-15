const URL = 'https://es.wikipedia.org/w/api.php';
const SEARCH_PARAMS = {
    action: 'query',
    list: 'search',
    srsearch: 'hongos comestibles',
    srlimit: 50,
    format: 'json',
    origin: '*'
};

async function buscarHongosComestibles() {
    try {
        const queryString = new URLSearchParams(SEARCH_PARAMS).toString();
        const apiUrl = `${URL}?${queryString}`;

        const response = await fetch(apiUrl);
        if (!response.ok) {
            throw new Error(`Error en la solicitud: ${response.status}`);
        }
        
        const data = await response.json();
        mostrarResultados(data.query.search);
    } catch (error) {
        console.error('Error:', error);
    }
}

async function mostrarResultados(searchResults) {
    const resultsList = document.getElementById('results');
    resultsList.innerHTML = ''; // Limpiar resultados anteriores

    try {
        for (const result of searchResults) {
            const colDiv = document.createElement('div');
            colDiv.classList.add('col', 'md-4', 'mb-3');

            const cardDiv = document.createElement('div');
            cardDiv.classList.add('card', 'h-100');

            const cardHeader = document.createElement('div');
            cardHeader.classList.add('card-header');
            cardHeader.textContent = result.title; //titulo
            cardDiv.appendChild(cardHeader);

            const pageParams = {
                action: 'query',
                titles: result.title,
                prop: 'pageimages|extracts',
                exintro: true,
                explaintext: true,
                pithumbsize: 100,
                format: 'json',
                origin: '*'
            };
            const pageQueryString = new URLSearchParams(pageParams).toString();
            const pageApiUrl = `${URL}?${pageQueryString}`;

            const pageResponse = await fetch(pageApiUrl);
            if (!pageResponse.ok) {
                throw new Error(`Error en la solicitud: ${pageResponse.status}`);
            }

            const pageData = await pageResponse.json();
            const pages = pageData.query.pages;
            const page = Object.values(pages)[0];

            const img = document.createElement('img');
            img.classList.add('card-img-top');
            
            img.src = page.thumbnail ? page.thumbnail.source : 'https://upload.wikimedia.org/wikipedia/commons/a/ac/No_image_available.svg';

           

            img.alt = `Imagen de ${result.title}`;
            cardDiv.appendChild(img);

            const cardBody = document.createElement('div');
            cardBody.classList.add('card-body');

            const extract = page.extract.length > 100 ? page.extract.substring(0, 100) + '...' : page.extract; // Limitar la descripción a 100 caracteres
            const bodyContent =  extract;
            cardBody.innerHTML = `<p class="card-text">${bodyContent}</p>`;
            cardDiv.appendChild(cardBody);

            const cardFooter = document.createElement('div');
            cardFooter.classList.add('card-footer');
            const verMasBtn = document.createElement('a');
            verMasBtn.href = 'detalles';
            verMasBtn.classList.add('btn', 'btn-primary', 'btn-block', 'mt-3');
            verMasBtn.textContent = 'Ver más info';

            verMasBtn.addEventListener('click', () => {
                const details = {
                    title: result.title,            
                    imgSrc: page.thumbnail ? page.thumbnail.source :'https://upload.wikimedia.org/wikipedia/commons/a/ac/No_image_available.svg',
                    extract: page.extract
                };
                localStorage.setItem('hongoDetalles', JSON.stringify(details));
            });

            cardFooter.appendChild(verMasBtn);
            cardDiv.appendChild(cardFooter);

            colDiv.appendChild(cardDiv);
            resultsList.appendChild(colDiv);
        }
    } catch (error) {
        console.error('Error:', error);
    }
}

document.addEventListener('DOMContentLoaded', buscarHongosComestibles);
