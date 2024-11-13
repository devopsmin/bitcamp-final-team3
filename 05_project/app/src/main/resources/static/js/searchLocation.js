// searchLocation.js
const searchElements = {
    input: null,
    icon: null,
    resultsContainer: null,
    clearButton: null
};

function initializeSearch() {
    searchElements.input = document.querySelector('#addLocationDiv .form-control');
    searchElements.icon = document.querySelector('#addLocationDiv .bi-search');
    searchElements.resultsContainer = document.querySelector('#addLocationDiv .mb-2');

    // x 버튼 생성
    const inputGroup = searchElements.input.parentElement;
    searchElements.clearButton = document.createElement('span');
    searchElements.clearButton.className = 'input-group-text bg-white border-start-0 cursor-pointer';
    searchElements.clearButton.innerHTML = '<i class="bi bi-x-lg text-muted"></i>';
    searchElements.clearButton.style.cursor = 'pointer';
    searchElements.clearButton.style.display = 'none';
    inputGroup.appendChild(searchElements.clearButton);
}

function performSearch(searchText) {
    let resultsHTML = '';
    for (let i = 1; i <= 10; i++) {
        resultsHTML += `
            <div class="card mb-2">
                <div class="card-body d-flex align-items-center justify-content-between gap-1">
                    <img alt="Location Image" class="rounded me-3"
                         src="/images/default.jpg"
                         style="width: 70px; height: 70px; object-fit: cover;">

                    <div class="d-flex flex-column" style="flex: 1;">
                        <h5 class="card-title mb-1" style="font-size: 16px;">검색결과 ${i}: ${searchText}</h5>
                        <p class="card-text text-muted mb-0" style="font-size: 12px;">주소 예시 ${i}</p>
                    </div>

                    <button class="btn btn-outline-secondary btn-location d-flex justify-content-center align-items-center"
                            data-index="${i-1}"
                            style="height: 60px; width: 30px;">
                        <i class="bi bi-plus-lg"></i>
                        <span class="location-x d-none">126.${i}</span>
                        <span class="location-y d-none">37.${i}</span>
                    </button>
                </div>
            </div>
        `;
    }
    searchElements.resultsContainer.innerHTML = resultsHTML;
}

function handleInputChange() {
    searchElements.clearButton.style.display = searchElements.input.value ? 'flex' : 'none';
}

function clearSearch() {
    searchElements.input.value = '';
    searchElements.clearButton.style.display = 'none';
    searchElements.resultsContainer.innerHTML = '';
}
